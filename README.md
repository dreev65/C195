DBClientApp - version 1.0

Purpose of application:
- Provides a GUI for scheduling, deleting and viewing customers and appointments
- Allows reporting of certain data for easier use management

Author: Daniel Reeve 
Student ID: 001990892 
Contact Information: dreev65@wgu.edu
Date: 12/10/22
--------------------------------------------------------------------------------------------------------------------

Application Specifications:

- IDE Product Version: Apache NetBeans IDE 14
- JDK Version: Java SE 17.0.4
- JavaFX Version: JavaFX-SDK-17.0.1
- MySQL Connector Version: mysql-connector-java-8.0.31

--------------------------------------------------------------------------------------------------------------------

Directions:

- Opon running the application user will be prompted to login in. Upon successful login, user will be directed to 
	the directory page
- From the directory page, the user can click one of the buttons on screen to access the customers list, 
	appointments list, or reports




#Customers:
- Once on the customers page, the user can view the customer list in the table, delete customers, add new customers,
	or update existing customers

##Delete Customer: 
- Users must select a customer record from the table before clicking the "Delete Customer" buton
- User will then be prompted to confirm the deletion of the customer
- If appointments are tied to the customer, user will be prompted to delete those first
- Once confirmed, user will be redirected to the customers window


##Add Customer:
- Users must enter data in all given fields before clicking the save button in order to create new customers
	(user will then be redirected back to customers window)
- Customer creation can be canceled by click the cancel button at anytime (user will then be redirected back to 
	customers window)

##Update Customer:
- Users must select a customer record from the table before clicking the "Update Customer" button
- Users must enter data in all given fields before clicking the save button in order to update selected customer
	(user will then be redirected back to customers window)
- Customer update can be canceled by click the cancel button at anytime (user will then be redirected back to 
	customers window)




#Appointments:
- Once on the customers page, the user can view the customer list in the table, delete customers, add new customers,
	or update existing customers

##Delete Appointment:
- Users must select an appointment record before clicking the "Delete Appointment" button
- User will then be prompted to confirm the deletion of the appointment
- Once confirmed, user will be redirected to the appointments window

##Add Appointment:
- Users must enter data in all given fields before clicking the save button in order to create the new appointment
	(user will then be redirected back to appointments window)
- Appointment creation can be canceled by click the cancel button at anytime (user will then be redirected back to 
	appointments window)

##Update Appointment:
- Users must select an appointment record before clicking the "Update Appointment" button
- Users must enter data in all given fields before clicking the save button in order to update selected appointment
	(user will then be redirected back to customers window)
- Appointment update can be canceled by click the cancel button at anytime (user will then be redirected back to 
	appointments window)



--------------------------------------------------------------------------------------------------------------------
#Reports:
- In the reports window, the user can select the report they want to view by clicking the tab with the appropriate
	label (reports are auto generated)
- "Contact Schedule" report is automatically filtered by the start datetime and contact name
- "Appointments by Month/Type" is automatically filtered by start datetime and type

##Additional Report: 

- The additional report supplied shows a list of the deleted appointments with the appointmentID, userID, 
	customerID, and appointment type
- The report was created to keep track of the appointments being deleted with some detail as to whom deleted it 
	and what appointment was deleted, within the current application session
