package com.hs.dianping.service.Impl;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.dal.UserModelMapper;
import com.hs.dianping.model.UserModel;
import com.hs.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserModelMapper userModelMapper;

    @Override
    public UserModel getUser(Integer id) {
        return userModelMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public UserModel register(UserModel resgisterUser) throws BusinessException, NoSuchAlgorithmException {
        System.out.println(resgisterUser.getPassword());
        resgisterUser.setPassword(encodeByMD5(resgisterUser.getPassword()));
        resgisterUser.setCreatedAt(new Date());
        resgisterUser.setUpdatedAt(new Date());

        try {
            userModelMapper.insertSelective(resgisterUser);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EnumBusinessError.REGISTER_DUP_FAIL);
        }
        return getUser(resgisterUser.getId());
    }

    @Override
    public UserModel login(String telphone, String password) throws NoSuchAlgorithmException, BusinessException {
        UserModel userModel = userModelMapper.selectByTelphoneAndPassword(telphone, encodeByMD5(password));
        if(userModel==null){
            throw new BusinessException(EnumBusinessError.LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public Integer countAllUSer() {
        return userModelMapper.countAllUser();
    }

    private String encodeByMD5(String pass) throws NoSuchAlgorithmException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        return Arrays.toString(Base64Coder.encode(messageDigest.digest(pass.getBytes(StandardCharsets.UTF_8))));
    }
}
