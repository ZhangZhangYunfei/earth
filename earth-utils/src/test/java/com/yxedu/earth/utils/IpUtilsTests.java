package com.yxedu.earth.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class IpUtilsTests {

    public void testIpv4Address() {
        String ipv4Address = IpUtils.ipv4Address();
        Assert.assertNotNull(ipv4Address);
    }
}
