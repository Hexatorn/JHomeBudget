package hexatorn.data;



import javafx.beans.property.*;

import java.time.LocalDate;

public class Bill {

    private  StringProperty place;
    private  StringProperty goodsOrServices;
    private  DoubleProperty amount;
    private  StringProperty category;
    private  StringProperty subcategory;
    private  ObjectProperty<LocalDate> date;
    private  StringProperty description;
    private  ObjectProperty<Person> person;


    public Bill(String place, String goodsOrService, double amount, LocalDate date, String category, String subcategory, Person person, String description) {
        this(place,goodsOrService,amount,date,category,subcategory,person);
        setDescription(description);
    }

    public Bill(String place,String goodsOrService, double amount, LocalDate date, String category, String subcategory, Person person ) {
        this(place,goodsOrService, amount, date, category, subcategory );
        this.person = new SimpleObjectProperty<Person>(person);
    }

    public Bill(String place,String goodsOrService, double amount, LocalDate date, String category, String subcategory){
        this.place = new SimpleStringProperty(place);
        this.goodsOrServices = new SimpleStringProperty(goodsOrService);
        this.amount = new SimpleDoubleProperty(amount);
        this.category = new SimpleStringProperty(category);
        this.subcategory = new SimpleStringProperty(subcategory);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.description = new SimpleStringProperty("");
        this.person = new SimpleObjectProperty<Person>(new Person());
    }


    public String getPlace() {
        return place.get();
    }
    public StringProperty placeProperty() {
        return place;
    }
    public void setPlace(String place) {
        this.place.set(place);
    }

    public double getAmount() {
        return amount.get();
    }
    public DoubleProperty amountProperty() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getCategory() {
        return category.get();
    }
    public StringProperty categoryProperty() {
        return category;
    }
    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getSubcategory() {
        return subcategory.get();
    }
    public StringProperty subcategoryProperty() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory.set(subcategory);
    }

    public LocalDate getDate() {
        return date.get();
    }
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public void setDescription(String description) {
        this.description.set(description);
    }


    public Person getPerson() {
        return person.get();
    }
    public ObjectProperty<Person> personProperty() {
        return person;
    }
    public void setPerson(Person person) {
        this.person.set(person);
    }

    public String getGoodsOrServices() {
        return goodsOrServices.get();
    }
    public StringProperty goodsOrServicesProperty() {
        return goodsOrServices;
    }
    public void setGoodsOrServices(String goodsOrServices) {
        this.goodsOrServices.set(goodsOrServices);
    }

    @Override
    public String toString() {
        return ""+getPlace()+"\t"+getGoodsOrServices()+"\t"+getDate()+"\t"+getAmount()+"\t"+getCategory()+"\t"+getSubcategory()+"\t"+getDescription()+"\t"+getPerson()+"\n";
    }
}
