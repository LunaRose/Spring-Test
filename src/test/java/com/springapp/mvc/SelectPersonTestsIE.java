package com.springapp.mvc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SelectPersonTestsIE
{

    private static EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("db-schema.sql").build();
    public static final String SELECT_FROM_PEOPLE = "SELECT id, name, age, sex, img FROM people";
    private static WebDriver ieDriver;
    private Person animal;

    public SelectPersonTestsIE(Person animal)
    {
        this.animal = animal;
    }

    @Parameterized.Parameters
    public static Collection<Person[]> data() {

        final List<Person[]> data = new LinkedList<Person[]>();
        final JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);
        final List<Person> people =  jt.query(SELECT_FROM_PEOPLE, new RowMapper<Person>()
        {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                final Person p = new Person();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setSex(Person.Sex.valueOf(rs.getString("sex")));
                p.setImg(rs.getString("img"));
                return p;
            }
        });

        final WebDriver ieDriver = new InternetExplorerDriver();
        final WebDriver firefoxDriver = new FirefoxDriver();
        final WebDriver chromeDriver = new ChromeDriver();

//        final List<WebDriver> drivers = new Arrays.asList(new WebDriver[]{ieDriver, firefoxDriver, chromeDriver});

        for (Person person : people)
        {
            data.add(new Person[]{person});
        }
        return data;
    }

    @Test
    public void selectPersonTests()
    {
        final WebElement tr = ieDriver.findElement(By.id("tr" + animal.getName()));
        tr.click();
        final WebElement selectedPerson = ieDriver.findElement(By.id("selectedPerson"));
        final WebElement personImage = ieDriver.findElement(By.id("selectedPersonImage"));
        final String imageUrl = animal.getImg().contains("http://") ? "" : "http://localhost:8080/img/";
        final Boolean selectedPersonImage = personImage.getAttribute("src").equals(imageUrl + animal.getImg());
        final Boolean selectedPersonText = selectedPerson.getText().equals("You picked: " + animal.getName() + " who is " + animal.getSex() + " and is " + animal.getAge() + " years old");
        assertTrue(selectedPersonImage && selectedPersonText);
    }

    @Test
    public void selectPersonByDropDown()
    {
        final WebElement button = ieDriver.findElement(By.id("personSelector" + animal.getSex().getValue()));
        button.click();
        final WebElement li = ieDriver.findElement(By.id("li" + animal.getName()));
        li.click();
        final WebElement selectedPerson = ieDriver.findElement(By.id("selectedPerson"));
        final WebElement personImage = ieDriver.findElement(By.id("selectedPersonImage"));
        final String imageUrl = animal.getImg().contains("http://") ? "" : "http://localhost:8080/img/";
        final Boolean selectedPersonImage = personImage.getAttribute("src").equals(imageUrl + animal.getImg());
        final Boolean selectedPersonText = selectedPerson.getText().equals("You picked: "+ animal.getName() + " who is " + animal.getSex() + " and is " + animal.getAge() + " years old");
        assertTrue(selectedPersonImage && selectedPersonText);
    }

    @BeforeClass
    public static void setUp()
    {
        final File file = new File("C:/IEDriverServer.exe");
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        ieDriver = new InternetExplorerDriver();
        ieDriver.get("http://localhost:8080");
    }

    @AfterClass
    public static void tearDown()
    {
        ieDriver.quit();
    }
}