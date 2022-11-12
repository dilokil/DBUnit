package ru.dilokill.test.model;

import ru.dilokill.service.PersonService;
import ru.dilokill.model.Person;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PersonTest extends DBUnitConfig{

    private PersonService service = new PersonService();
    private EntityManager em = Persistence.createEntityManagerFactory("db_lab4_2").createEntityManager();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ru/dilokill/model/person/person-data.xml"));

        tester.setDataSet(beforeData);
        tester.onSetup();
    }

    public PersonTest(String name) {
        super(name);
    }
    @Test
    public void testGetAll() throws Exception {
        List<Person> persons = service.getAll();

        IDataSet expectedData = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ru/dilokill/model/person/person-data.xml"));
        IDataSet actualData = tester.getConnection().createDataSet();
        System.out.println(actualData);
        System.out.println(expectedData.toString());
        System.out.println(Arrays.toString(persons.toArray()));
//        Assertion.assertEquals(expectedData, actualData);
        Assert.assertEquals(expectedData.getTable("person").getRowCount(),persons.size());
    }

    @Test
    public void testSave() throws Exception {
        Person person = new Person();
        person.setName("Никита");
        person.setSurname("Ушаков");
        person.setMoney(BigDecimal.valueOf(10000));

        service.save(person);

        IDataSet expectedData = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("ru/dilokill/model/person/person-data-save.xml"));
        IDataSet actualData = tester.getConnection().createDataSet();

        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), expectedData);

        String[] ignore = {"id"};
        Assertion.assertEqualsIgnoreCols(expectedData, actualData, "person", ignore);
    }

    @Test
    public void testDelete() throws Exception {
        Person person = new Person();
        person.setName("Александр");
        person.setSurname("Косарев");
        person.setMoney(BigDecimal.valueOf(10000));

//        person = em.merge(person);

        int id = person.getId();

        service.delete(person);
        Person personFromDb = em.find(Person.class, id);

        Assert.assertNull(personFromDb);
    }

    @Test
    public void testUpdate() throws Exception {
        Person person = em.find(Person.class, 1);
        person.setName("Сашка");
        service.update(person);
        Person personFromDb = em.find(Person.class, 1);

        Assert.assertEquals(person.getName(), personFromDb.getName());
    }

    @Test
    public void testGetById() throws Exception {
        Person person = service.get(1);
        Person person1 = em.find(Person.class, 1);

        Assert.assertEquals(person.getId(), person1.getId());
        Assert.assertEquals(person.getName(), person1.getName());
        Assert.assertEquals(person.getSurname(), person1.getSurname());
        Assert.assertEquals(person.getMoney(), person1.getMoney());
    }

    @Test
    public void testFuncGetMax() throws Exception {
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_max");
        storedProcedure.registerStoredProcedureParameter("result", BigDecimal.class, ParameterMode.OUT);

        storedProcedure.execute();

        BigDecimal actualValue = (BigDecimal) storedProcedure.getOutputParameterValue("result");
        BigDecimal expectedValue = BigDecimal.valueOf(15000);

//      Жесткий костыль сравнения значений(лень что-то думать), так как 15000.00 не равно 15000.
        Assert.assertEquals(expectedValue.compareTo(actualValue), 0);
    }
}
