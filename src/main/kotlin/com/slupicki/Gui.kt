package com.slupicki

import com.vaadin.annotations.Push
import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.annotation.WebServlet

@Theme("mytheme")
@Push
@SpringUI
class Gui : UI() {
    val log = LoggerFactory.getLogger(this.javaClass)!!

    @Autowired
    lateinit var repository: PersonRepository

    var persons: List<Person> = emptyList()

    override fun init(request: VaadinRequest) {
        val layout = VerticalLayout()
        val form = FormLayout()
        val grid = Grid<Person>()

        layout.defaultComponentAlignment = Alignment.TOP_CENTER
        layout.setHeightUndefined()
        layout.addComponent(form)
        layout.addComponent(grid)
        content = layout

        form.setWidth(null)

        val firstNameTF = TextField("First name")
        val lastNameTF = TextField("Last name")
        val addButton = Button("Add") { _ ->
            val newPerson = Person(firstNameTF.value, lastNameTF.value)
            repository.save(newPerson)
            refreshGrid(grid)
            log.info("Add $newPerson")
            firstNameTF.value = ""
            lastNameTF.value = ""
        }
        val filterByLastNameButton = Button("Filter by last name") { _ ->
            val lastName = lastNameTF.value
            persons = (if (lastName.isEmpty())
                repository.findAll()
            else
                repository.findByLastName(lastName)
                    ).toList()
            refreshGrid(grid, persons)
            log.info("Filter by last name '$lastName'")
        }

        form.addComponent(firstNameTF)
        form.addComponent(lastNameTF)
        form.addComponent(addButton)
        form.addComponent(filterByLastNameButton)

        grid.addColumn { persons.indexOf(it) + 1 }
        grid.addColumn(Person::firstName).caption = "First name"
        grid.addColumn(Person::lastName).caption = "Last name"
        grid.frozenColumnCount = 1

        refreshGrid(grid)
    }

    fun refreshGrid(grid: Grid<Person>) {
        persons = repository.findAll().toList()
        refreshGrid(grid, persons)
    }

    fun refreshGrid(grid: Grid<Person>, persons: List<Person>) {
        grid.setItems(persons)
    }
}

@WebServlet(urlPatterns = arrayOf("/*"), name = "GuiServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = Gui::class, productionMode = false)
class GuiServlet : VaadinServlet()