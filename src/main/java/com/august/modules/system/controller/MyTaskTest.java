package com.august.modules.system.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.controller
 * Author: August
 * Update: August(2015/9/29)
 * Description:
 */
@Component
public class MyTaskTest {
    public final Logger logger = Logger.getLogger(this.getClass());

    public void test01() {
        for (int i = 0; i < 5; i++) {
            logger.debug(i + " 运行开始......................................" + (new Date()));
        }
    }
}
