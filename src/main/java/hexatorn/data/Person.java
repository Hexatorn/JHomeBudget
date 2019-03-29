package hexatorn.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private StringProperty name;
    private StringProperty lastName;
    private IntegerProperty age;


    public Person(String name, String lastName, int age) {
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty(age);
    }

    public Person(String name) {
        this(name,"",0);
    }

    public Person(String name, String lastName) {
        this(name,lastName,0);
    }

    Person(){
        this("someone","",0);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    //TODO stworzyć kilka wariantów wyświetlania
    @Override
    public String toString() {
        return getName();
    }
    public StringProperty toStringPropherty(){
        return nameProperty();
    }
}
