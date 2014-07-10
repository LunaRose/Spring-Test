package com.springapp.mvc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class AddPersonTests
{

    private static WebDriver firefoxDriver;
    private Person animal;

    public AddPersonTests(Person animal)
    {
        this.animal = animal;
    }

    @Parameterized.Parameters
    public static Collection<Person[]> data() {

        final List<Person[]> data = new LinkedList<Person[]>();

        final Person testPerson = new Person();
        testPerson.setName("Test McTesterson");
        testPerson.setAge(30);
        testPerson.setSex(Person.Sex.Male);
        testPerson.setImg("http://www.gibedigital.com/media/1280/unit-test.jpg");

        data.add(new Person[]{testPerson});

        final Person testPerson2 = new Person();
        testPerson2.setName("Terry TestSkeptic");
        testPerson2.setAge(26);
        testPerson2.setSex(Person.Sex.Female);
        testPerson2.setImg("http://www.quickmeme.com/img/35/357dfeea82f1935e96e660b27a1d87065194c92e75fd6e7090ffc3bb18550290.jpg");

        data.add(new Person[]{testPerson2});

        return data;
    }

    @Test
    public void addPersonTests()
    {
        final WebElement fullName = firefoxDriver.findElement(By.id("addFullName"));
        fullName.sendKeys(animal.getName());

        final WebElement age = firefoxDriver.findElement(By.id("addAge"));
        age.sendKeys(Integer.toString(animal.getAge()));

        final WebElement sex = firefoxDriver.findElement(By.id("addSex"));
        sex.sendKeys(animal.getSex().getValue());

        final WebElement img = firefoxDriver.findElement(By.id("addImg"));
        img.sendKeys(animal.getImg());

        img.submit();

        final Alert alert = firefoxDriver.switchTo().alert();

        final Boolean addPersonSuccess = alert.getText().equals("Successfully added new person!");

        alert.accept();

        assertTrue(addPersonSuccess);
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