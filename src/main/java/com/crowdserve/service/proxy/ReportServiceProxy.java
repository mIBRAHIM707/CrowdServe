package com.crowdserve.service.proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.crowdserve.service.ReportService; //This is the service interface
import com.crowdserve.service.impl.ReportServiceImpl;


//It is a bean which is used to instantiate the object by the Spring boot which stores in the Application Context.
@Service 
@Primary 
//When SpringBoot runs, it sees that there are two classes that have been marked @Service.
//So it creates a definition list first and then put these two classes in the definition list.
//Since I hvae written @Lazy with the reportServiceImpl, it will not be instantiated but it will be inside the definition list.
//Only the object of the reportServiceProxy will be instantiated.
//In the controller I have written @Autowired with the ReportService interface.
//So spring sees the definition list and not the object list.
//So it will produce error.
//To resolve this, we have written @Primary with the reportServiceProxy.


public class ReportServiceProxy implements ReportService{
    
    @Autowired
    @Lazy
    private ReportServiceImpl reportServiceImpl;

    @Override
    public byte[] generateCompletedTasksReport() {
        return reportServiceImpl.generateCompletedTasksReport();
    }
 
    @Override
    public byte[] generateTopContributorsReport() {
        return reportServiceImpl.generateTopContributorsReport();
    }

}
