package com.n26.CodeChallenge.test.unit;

import static org.junit.Assert.assertFalse;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class ModelAccessorsTest {

  // The package to be tested
  private List<PojoClass> pojoClasses;


  @Before
  public void setup() {
    pojoClasses = PojoClassFactory.getPojoClasses("com.n26.CodeChallenge.model");

  }

  @Ignore
  @Test
  public void allFieldsShouldBeReadAndWritable() {
    Validator validator =
        ValidatorBuilder.create().with(new SetterMustExistRule(), new GetterMustExistRule())
            .with(new SetterTester(), new GetterTester()).build();
    validator.validate(pojoClasses);
  }

  @Ignore
  @Test
  public final void allClassesShouldImplementToString() {
    pojoClasses.stream().forEach(x -> {
      try {
        Object o = x.getClazz().newInstance();
        assertFalse(o.toString().contains("@"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }


}
