package com.blackshoe.esthetecoreservice.config;

import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

public class DataModelSerializer implements RedisSerializer<GenericDataModel> {


    @Override
    public byte[] serialize(GenericDataModel dataModel) throws SerializationException {
        if (dataModel == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(dataModel);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("DataModel 직렬화 실패", e);
        }
    }

    @Override
    public GenericDataModel deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (GenericDataModel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("DataModel 역직렬화 실패", e);
        }
    }
}
