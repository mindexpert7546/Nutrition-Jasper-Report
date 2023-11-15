package com.iinterchange

class BootStrap {

    def init = { servletContext ->

    def profileData = new ProfileDetails(name:"Kundan Kumar",dob:"23/12/2001",age:50)
    profileData.save()
    }
    def destroy = {
    }
}
