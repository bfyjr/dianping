package com.hs.dianping.service;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.model.UserModel;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    UserModel getUser(Integer id);

    UserModel register(UserModel resgisterUser) throws BusinessException, NoSuchAlgorithmException;

    UserModel login(String telphone,String password) throws NoSuchAlgorithmException, BusinessException;

    Integer countAllUSer();
}
