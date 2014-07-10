package com.springapp.mvc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SelectPersonTests
{

    private static EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("db-schema.sql").build();
    public static final String SELECT_FROM_PEOPLE = "SELECT id, name, age, sex, img FROM people";
    private static WebDriver firefoxDriver;
    private Person animal;

    public SelectPersonTests(Person animal)
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

        for (Person person : people)
        {
            data.add(new Person[]{person});
        }
        return data;
    }

    @Test
    public void selectPersonTests()
    {
        final WebElement tr = firefoxDriver.findElement(By.id("tr" + animal.getName()));
        tr.click();
        final WebElement selectedPerson = firefoxDriver.findElement(By.id("selectedPerson"));
        final WebElement personImage = firefoxDriver.findElement(By.id("selectedPersonImage"));
        final String imageUrl = animal.getImg().contains("http://") ? "" : "http://localhost:8080/img/";
        final Boolean selectedPersonImage = personImage.getAttribute("src").equals(imageUrl + animal.getImg());
        final Boolean selectedPersonText = selectedPerson.getText().equals("You picked: " + animal.getName() + " who is " + animal.getSex() + " and is " + animal.getAge() + " years old");
        assertTrue(selectedPersonImage && selectedPersonText);
    }

    @Test
    public void selectPersonByDropDown()
    {
        final WebElement button = firefoxDriver.findElement(By.id("personSelector" + animal.getSex().getValue()));
        button.click();
        final WebElement li = firefoxDriver.findElement(By.id("li" + animal.getName()));
        li.click();
        final WebElement selectedPerson = firefoxDriver.findElement(By.id("selectedPerson"));
        final WebElement personImage = firefoxDriver.findElement(By.id("selectedPersonImage"));
        final String imageUrl = animal.getImg().contains("http://") ? "" : "http://localhost:8080/img/";
        final Boolean selectedPersonImage = personImage.getAttribute("src").equals(imageUrl + animal.getImg());
        final Boolean selectedPersonText = selectedPerson.getText().equals("You picked: "+ animal.getName() + " who is " + animal.getSex() + " and is " + animal.getAge() + " years old");
        assertTrue(selectedPersonImage && selectedPersonText);
    }

    @BeforeClass
    public static void setUp()
    {
        firefoxDriver = new FirefoxDriver();
        firefoxDriver.get("http://localhost:8080");
    }

    @AfterClass
    public static void tearDown()
    {
        firefoxDriver.quit();
    }
}