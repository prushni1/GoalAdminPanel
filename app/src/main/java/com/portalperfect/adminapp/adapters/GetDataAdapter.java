package com.portalperfect.adminapp.adapters;


public class GetDataAdapter {

    int Id;
    String name;
    String phone_number;
    String subject;
    String city;

    String newid,testresult;

    public String getTestresult() {
        return testresult;
    }

    public void setTestresult(String testresult) {
        this.testresult = testresult;
    }

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getId() {

        return Id;
    }

    public void setId(int Id1) {

        this.Id = Id1;
    }


    public String getPhone_number() {

        return phone_number;
    }

    public void setPhone_number(String phone_number1) {

        this.phone_number = phone_number1;
    }

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject1) {

        this.subject = subject1;
    }

}
