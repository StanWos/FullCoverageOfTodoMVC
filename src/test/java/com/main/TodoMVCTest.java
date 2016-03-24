package com.main;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.main.TodoMVCTest.TaskType.*;

/**
 * Created by stan on 21.03.16.
 */
public class TodoMVCTest {

    @Before
    public void openPage() {
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testTaskLifeCycle() {

        add("1");
        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");

        filterActive();
        assertTasks("1 edited");
        toggleAll();
        add("2");
        assertVisibleTasks("2");

        filterCompleted();
        assertVisibleTasks("1 edited");
        clearCompleted();
        assertNoVisibleTasks();

        filterAll();
        assertItemLeft(1);
        assertTasks("2");

        delete("2");
        assertNoTasks();

    }

    // *** All filter ***

    @Test
    public void testCompleteAtAll() {
        given(aTask("1", ACTIVE));

        toggle("1");
        assertTasks("1");
        assertItemLeft(0);
    }

    @Test
    public void testCompleteAllAtAll() {
        givenAtAll(ACTIVE, "1", "2");

        toggleAll();
        assertTasks("1", "2");
        assertItemLeft(0);
    }

    @Test
    public void testReopenAtAll() {
        given(aTask("1", COMPLETED), aTask("2", ACTIVE));

        //reopen
        toggle("1");
        assertTasks("1", "2");
        assertItemLeft(2);
    }

    @Test
    public void testReopenAllAtAll() {
        givenAtAll(COMPLETED, "1", "2");

        toggleAll();
        assertTasks("1", "2");
        assertItemLeft(2);
    }

    @Test
    public void testEditCancelledAtAll() {
        given(aTask("1", ACTIVE));

        startEdit("1", "1 edited cancelled").pressEscape();
        assertTasks("1");
        assertItemLeft(1);
    }

    @Test
    public void testClearCompletedAtAll() {
        givenAtAll(COMPLETED, "1", "2");

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testEditByClickOutsideAtAll() {
        given(aTask("1", ACTIVE));

        startEdit("1", "1 edited");
        filterActive();
        assertVisibleTasks("1 edited");
        assertItemLeft(1);
    }

    @Test
    public void testEditByClickTabAtAll() {
        given(aTask("1", ACTIVE));

        startEdit("1", "1 edited").pressTab();
        assertVisibleTasks("1 edited");
        assertItemLeft(1);
    }

    @Test
    public void testEmptyingEditedTextAtAll() {
        given(aTask("1", ACTIVE));

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }

    // *** Active filter ***

    @Test
    public void testEditAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        startEdit("1", "1 edited").pressEnter();
        assertTasks("1 edited");
        assertItemLeft(1);
    }

    @Test
    public void testDeleteAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        delete("1");
        assertNoTasks();
    }

    @Test
    public void testCompleteAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        toggle("1");
        assertNoVisibleTasks();
        assertItemLeft(0);
    }

    @Test
    public void testReopenAllAtActive() {
        givenAtActive(COMPLETED, "1", "2");

        toggleAll();
        assertVisibleTasks("1", "2");
        assertItemLeft(2);
    }

    @Test
    public void testEditCancelledAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        startEdit("1", "1 edited cancelled").pressEscape();
        assertTasks("1");
        assertItemLeft(1);
    }

    @Test
    public void testClearCompletedAtActive() {
        givenAtActive(COMPLETED, "1", "2");

        clearCompleted();
        assertNoVisibleTasks();
    }

    @Test
    public void testEditByClickOutsideAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        startEdit("1", "1 edited");
        filterAll();
        assertTasks("1 edited");
        assertItemLeft(1);
    }

    @Test
    public void testEditByClickTabAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        startEdit("1", "1 edited").pressTab();
        assertVisibleTasks("1 edited");
        assertItemLeft(1);
    }

    @Test
    public void testEmptyingEditedTextAtActive() {
        givenAtActive(aTask("1", ACTIVE));

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }

    // *** Completed filter ***

    @Test
    public void testAddAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        assertTasks("1");
        assertItemLeft(0);
    }

    @Test
    public void testEditAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        startEdit("1", "1 edited").pressEnter();
        assertVisibleTasks("1 edited");
        assertItemLeft(0);
    }

    @Test
    public void testCompleteAllAtCompleted() {
        givenAtCompleted(ACTIVE, "1", "2");

        toggleAll();
        assertVisibleTasks("1", "2");
        assertItemLeft(0);
    }

    @Test
    public void testReopenAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        toggle("1");
        assertNoVisibleTasks();
        assertItemLeft(1);
    }

    @Test //test on one type
    public void testReopenAllAtCompleted() {
        givenAtCompleted(COMPLETED, "1", "2");

        toggleAll();
        assertNoVisibleTasks();
        assertItemLeft(2);
    }

    @Test
    public void testEditCancelledAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        startEdit("1", "1 edited cancelled").pressEscape();
        assertTasks("1");
        assertItemLeft(0);
    }

    @Test
    public void testEditByClickOutsideAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        startEdit("1", "1 edited");
        filterAll();
        assertTasks("1 edited");
        assertItemLeft(0);
    }

    @Test
    public void testEditByClickTabAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemLeft(0);
    }

    @Test
    public void testEmptyingEditedTextAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }

    @Test
    public void testDeleteAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED));

        delete("1");
        assertNoTasks();
    }


    ElementsCollection tasks = $$("#todo-list li");

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void filterAll() {
        $(By.linkText("All")).click();
    }

    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    private void assertTasks(String... tasksTexts) {
        tasks.shouldHave(exactTexts(tasksTexts));
    }

    private void assertNoTasks() {
        tasks.shouldHave(empty);
    }

    private void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    private void assertItemLeft(int numberOfTasks) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(numberOfTasks)));
    }


    //  ******* Implementing Precondition-helpers *******

    public enum TaskType {
        ACTIVE, COMPLETED
    }

    public class Task {
        String taskText;
        TaskType taskType;

        public Task(String taskText, TaskType taskType) {
            this.taskText = taskText;
            this.taskType = taskType;
        }
    }

    public Task aTask(String taskText, TaskType taskType) {
        return new Task(taskText, taskType);
    }

    private void givenAtActive(Task... tasks) {
        given(tasks);
        filterActive();
    }

    private void givenAtCompleted(Task... tasks) {
        given(tasks);
        filterCompleted();
    }

    private void givenAtAll(TaskType mainType, String... taskTexts) {
        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < taskTexts.length; i++) {
            tasks[i] = new Task(taskTexts[i], mainType);
        }
        given(tasks);
    }

    private void givenAtActive(TaskType mainType, String... taskTexts) {
        givenAtAll(mainType, taskTexts);
        filterActive();
    }

    private void givenAtCompleted(TaskType mainType, String... taskTexts) {
        givenAtAll(mainType, taskTexts);
        filterCompleted();
    }

    private void given(Task... tasks) {
        String JavaS = "localStorage.setItem('todos-troopjs', '[";

        for (Task task : tasks) {
            JavaS += "{\"completed\":" + ((ACTIVE == task.taskType) ? "false" : "true") + ", \"title\":\"" + task.taskText + "\"},";
        }
        if (tasks.length > 0) {
            JavaS = JavaS.substring(0, (JavaS.length() - 1));
        }
        JavaS += "]');";
        executeJavaScript(JavaS);
        refresh();
    }

}
