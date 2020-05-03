package com.mao;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * zk 序列化，反序列化
 * @author bigdope
 * @create 2019-01-03
 **/
public class MyZkSerializer implements ZkSerializer {

    private String charset = "UTF-8";

    @Override
    public byte[] serialize(Object object) throws ZkMarshallingError {
        try {
            return String.valueOf(object).getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError(e);
        }
    }

}
