import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class RegisterTest {

    private Register register;

    @Before
    public void setUp() {
        register = new Register();
    }

    @Test()
    public void absentObjectThrowsException() {
        Optional<Object> optional = register.get("absent");
        Assert.assertFalse(optional.isPresent());
    }

    @Test
    public void canRegisterByTypeAndRetrieve() {
        Glucose glucose = new Glucose();
        register.add(glucose);
        Water water = new Water();
        register.add(water);
        Glucose retrieveGlucose = register.get(Glucose.class);
        Water retrieveWater = register.get(Water.class);
        Assert.assertSame(glucose, retrieveGlucose);
        Assert.assertSame(water, retrieveWater);
    }

    @Test(expected = RuntimeException.class)
    public void secondRegisterOfSameNameThrowsException() {
        register.add("same", new Object());
        register.add("same", new Object());
    }

    @Test
    public void patientShouldBeInjectedByGlucose() {
        Glucose glucose = new Glucose();
        register.add(new Patient());
        register.add(glucose);
        register.inject();
        Patient retrievedPatient = register.get(Patient.class);
        Glucose injectGlucose = retrievedPatient.getGlucose();
        Assert.assertNotNull(injectGlucose);
        Assert.assertSame(glucose, injectGlucose);

    }

    @Test
    public void canRegisterSomethingAndRetrieve() {
        Object one = new Object();
        Object two = new Object();
        register.add("one", one);
        register.add("two", two);
        Object retrieveOne = register.get("one");
        Object retrieveTwo = register.get("two");
        Assert.assertNotNull(retrieveOne);
        Assert.assertSame(one, retrieveOne);
        Assert.assertSame(two, retrieveTwo);
        Assert.assertNotSame(retrieveOne, retrieveTwo);

    }

    class NotInstantiableClass {
        private NotInstantiableClass(){};
    }

    @Test(expected = RuntimeException.class)
    public void notInstantiableThrowsException() {
        register.add(NotInstantiableClass.class);
    }
}
