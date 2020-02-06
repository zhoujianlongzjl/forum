package com.qingfengzhuyue.forum.service;

import com.qingfengzhuyue.forum.mapper.UserMapper;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;


    public List<User> login(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);

        return users;
    }
}
