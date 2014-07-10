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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class AddCommentTests
{

    private static final WebDriver firefoxDriver = new FirefoxDriver();;
    private final String name;
    private final String comment;

    public AddCommentTests(String name, String comment)
    {
        this.name = name;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data()
    {

        final List<String[]> data = new LinkedList<String[]>();

        data.add(new String[]{"Test McTesterson", "Testing is Awesome!"});
        data.add(new String[]{"Terry TestSkeptic", "Testing is over-rated"});

        return data;

    }

    @Test
    public void addPersonTests() throws InterruptedException
    {
        final WebElement commentName = firefoxDriver.findElement(By.id("commentName"));
        commentName.sendKeys(name);

        final WebElement commentText = firefoxDriver.findElement(By.id("commentText"));
        commentText.sendKeys(comment);

        commentText.submit();

        synchronized (firefoxDriver)
        {
            firefoxDriver.wait(2000);
        }

        final List<WebElement> comments = firefoxDriver.findElements(By.className("comment"));

        final WebElement lastComment = comments.get(comments.size() -1);

        final String finalCommentName = lastComment.findElement(By.tagName("h3")).getText();
        final String finalCommentText = lastComment.findElement(By.tagName("p")).getText();

        assertTrue(finalCommentName.equals(name) && finalCommentText.equals(comment));
    }

    @BeforeClass
    public static void setUp()
    {
        firefoxDriver.get("http://localhost:8080");
    }

    @AfterClass
    public static void tearDown()
    {
        firefoxDriver.quit();
    }
}