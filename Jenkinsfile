pipeline {
    agent any

    environment {
        // 환경 변수 설정
        IMAGE_NAME = 'lsb8375/esthete-core-service'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub_jenkins')
        JOB_NAME = 'esthete-core-service'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'pwd' // 현재 디렉토리 확인
            }
        }

        stage('Gradle Build') {
            steps {
                sh 'cd /var/jenkins_home/workspace/${JOB_NAME} && ./gradlew clean build -x test'
            }
        }

        stage('DockerHub Login') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' // docker hub 로그인
            }
        }

        stage('Determine Docker Image Version') {
            steps {
                script {
                    def lastImageVersion
                    def lastImageTag

                    if(fileExists("../${JOB_NAME}-image-tag.txt")) {
                        lastImageTag = readFile("../${JOB_NAME}-image-tag.txt").trim()
                    }else{
                        writeFile file: "../${JOB_NAME}-image-tag.txt", text: "1.0"
                        lastImageTag = "1.0"
                    }
                    lastImageVersion = lastImageTag.tokenize('.')
                    def majorVersion = lastImageVersion[0] as int
                    def minorVersion = lastImageVersion[1] as int

                    // 이미지 버전 증가
                    minorVersion += 1
                    if (minorVersion >= 10) {
                        majorVersion += 1
                        minorVersion = 0
                    }

                    env.IMAGE_TAG = "${majorVersion}.${minorVersion}"
                    currentBuild.description = "Docker 이미지 버전: ${env.IMAGE_TAG}"
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} --platform linux/amd64 -f /var/jenkins_home/workspace/${JOB_NAME}/Dockerfile /var/jenkins_home/workspace/${JOB_NAME}"
            }
        }

        stage('Docker Push to Docker Hub') {
            steps {
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Docker Clean Up') {
            steps {
                script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}" // 이미지 삭제
                    sh "docker image prune -f" // 사용하지 않는 이미지 삭제
                }
            }
        }
        stage('Update Image Version File') {
            steps {
                script {
                    writeFile file: "../${JOB_NAME}-image-tag.txt", text: env.IMAGE_TAG
                }
            }
        }

        stage('Update values.yaml on GitHub') {
            steps {
                script {
                    def githubToken = credentials('ghp_write_repo')
                    echo "githubToken: ${githubToken}"

                    def githubRepo = 'dgu-web-t3-blackshoe/esthete-gitops'

                    def filePath = 'esthete-charts/esthete-user-chart/values.yaml'

                    def newContents = """
# Default values for esthete-user-chart.
# This is a YAML-formatted file.
# Declare variables to be passed into your templates.

# esthete-deployment-chart/values.yaml

replicaCount: 1

image:
  repository: lsb8375/esthete-user-service
  tag: \${env.IMAGE_TAG}

containerPort: 8080

ingress:
  enabled: true

controller:
  ## Argo controller commandline flags
  args:
    appResyncPeriod: \"60\"
"""
                    def sha = sh(script: """
                    curl -s -X GET -H 'Authorization: token ${githubToken}' https://api.github.com/repos/${githubRepo}/contents/${filePath}?ref=deployment | jq -r '.sha'
                    """, returnStatus: true).trim()

                    def response = sh(script: """
curl -X PUT -H 'Authorization: token ${githubToken}' \\
-d '{
  "message": "Update values.yaml",
  "content": "\$(echo -n '${newContents}' | base64 -w0)",
  "sha": "$sha",
}' \\
'https://api.github.com/repos/${githubRepo}/contents/${filePath}?ref=deployment'
""", returnStatus: true)

                    if (response == 0) {
                        currentBuild.result = 'SUCCESS'
                    } else {
                        currentBuild.result = 'FAILURE'
                        error("GitHub의 values.yaml를 업데이트하지 못했습니다.")
                    }
                }
            }
        }
    }
}
