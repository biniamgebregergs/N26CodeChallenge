package com.n26.CodeChallenge.test.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.n26.CodeChallenge.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilTest {

  @Test
  public void testIsTransactionHappenedInThis60Seconds() {
    Assert.assertEquals(true,
        Util.transactionHappenedInLast60Seconds(System.currentTimeMillis() / 1000));
    Assert.assertEquals(true,
        Util.transactionHappenedInLast60Seconds(System.currentTimeMillis() / 1000 - 15));
    Assert.assertEquals(false,
        Util.transactionHappenedInLast60Seconds(System.currentTimeMillis() / 1000 - 60));
    Assert.assertEquals(false,
        Util.transactionHappenedInLast60Seconds(System.currentTimeMillis() / 1000 - 100));
  }

}
