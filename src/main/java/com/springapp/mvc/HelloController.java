package com.springapp.mvc;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/people")
public class HelloController {

    public static final String INSERT_INTO_PEOPLE = "INSERT INTO people (name, age, sex, img) VALUES (?, ?, ?, ?)";
    public static final String INSERT_INTO_COMMENTS = "INSERT INTO comments (author, commentText) VALUES (?, ?)";
    public static final String SELECT_FROM_PEOPLE = "SELECT id, name, age, sex, img FROM people";
    public static final String SELECT_FROM_PEOPLE_BY_SEX = "SELECT id, name, age, sex, img FROM people WHERE sex = ?";
    public static final String SELECT_FROM_COMMENTS = "SELECT author, commentText FROM comments";

    private EmbeddedDatabase embeddedDatabase= new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("db-schema.sql").build();

    private List<Person> getPersons()
    {
        final JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);
        return jt.query(SELECT_FROM_PEOPLE, new RowMapper<Person>()
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
    }

    private List<Person> getPersonBySex(String sex)
    {
        final JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);
        return jt.query(SELECT_FROM_PEOPLE_BY_SEX, new Object[]{sex} , new RowMapper<Person>()
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
    }

    private List<Comment> getComments()
    {
        final JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);
        return jt.query(SELECT_FROM_COMMENTS, new RowMapper<Comment>()
        {
            @Override
            public Comment mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                final Comment comment = new Comment();
                comment.setAuthor(rs.getString("author"));
                comment.setText(rs.getString("commentText"));
                return comment;
            }
        });
    }

    @RequestMapping(value = "/people", method = RequestMethod.GET)
    public @ResponseBody List<Person> printPeople() {
        return getPersons();
    }

    @RequestMapping(value = "/peopleBySex", method = RequestMethod.GET)
    public @ResponseBody List<Person> printPeopleBySex( @ModelAttribute(value = "sex") final String sex) {
        return getPersonBySex(sex);
    }

    @RequestMapping(value = "/getComments", method = RequestMethod.GET)
    public @ResponseBody List<Comment> printComments() {
        return getComments();
    }

    @RequestMapping(value = "/personFile", method = RequestMethod.GET)
    public void getPeople(HttpServletResponse response) {
        try {
            final Workbook workbook = new HSSFWorkbook();
            final Sheet people = workbook.createSheet("People");
            final Row row = people.createRow(people.getPhysicalNumberOfRows());
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Name");
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Age");
            row.createCell(row.getPhysicalNumberOfCells()).setCellValue("Sex");

            for (Person person : getPersons())
            {
                final Row personRow = people.createRow(people.getPhysicalNumberOfRows());
                personRow.createCell(personRow.getPhysicalNumberOfCells()).setCellValue(person.getName());
                personRow.createCell(personRow.getPhysicalNumberOfCells()).setCellValue(person.getAge());
                personRow.createCell(personRow.getPhysicalNumberOfCells()).setCellValue(person.getSex().getValue());
            }

            workbook.write(response.getOutputStream());
            response.setContentType("application/ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=people.xls");
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(value = "/best", method = RequestMethod.POST)
    public @ResponseBody Person bestPerson( @RequestBody final  Person person) {
        return person;
    }

    @RequestMapping(value = "/pick", method = RequestMethod.POST)
    public @ResponseBody Person pickPerson( @ModelAttribute(value = "personName") final String personName) {
        for (Person person : getPersons())
        {
            if (person.getName().equals(personName))
            {
                return person;
            }
        }
        return null;
    }

    @RequestMapping(value = "/addperson", method = RequestMethod.POST)
    public @ResponseBody Boolean addPerson( @RequestBody final  Person person)
    {
        final String sql = INSERT_INTO_PEOPLE;

        final JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);

        jt.update(sql, person.getName(), person.getAge(), person.getSex().name(), person.getImg());

        return true;
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public @ResponseBody Boolean addComment( @RequestBody final  Comment comment)
    {
        String sql = INSERT_INTO_COMMENTS;

        JdbcTemplate jt = new JdbcTemplate(embeddedDatabase);

        jt.update(sql, comment.getAuthor(), comment.getText());

        return true;
    }
}