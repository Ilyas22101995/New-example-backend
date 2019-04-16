package com.javasampleapproach.springrest.postgresql.controller;


import java.io.IOException;

import java.util.*;


import com.javasampleapproach.springrest.postgresql.config.TemplateLoader;
import com.javasampleapproach.springrest.postgresql.model.MyOwnEntity;
import com.javasampleapproach.springrest.postgresql.model.Person;
import com.javasampleapproach.springrest.postgresql.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import com.javasampleapproach.springrest.postgresql.model.Customer;
import com.javasampleapproach.springrest.postgresql.repo.CustomerRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	CustomerRepository repository;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	public JavaMailSender emailSender;

	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		System.out.println("Get all Customers...");

		List<Customer> customers = new ArrayList<>();
		repository.findAll().forEach(customers::add);

		return customers;
	}


	@PostMapping("/customers/create")
	public Customer postCustomer(@RequestBody Customer customer) throws MessagingException, IOException {

		Customer _customer = new Customer(customer.getLastname(), customer.getFirstname(), customer.getAge(),
				customer.getEmail());
		UUID uuidLink = UUID.randomUUID();
		_customer.setActivationCode(uuidLink.toString());
		repository.save(_customer);

		List<Person> personFromDb = personRepository.findByEmail(customer.getEmail());

		MimeMessage message = emailSender.createMimeMessage();

		String activationURL = "http://localhost:8080/api/activate/"+uuidLink.toString();
		String activationURLToPerson = "http://localhost:8080/api";

		String format = TemplateLoader.loadFilledTemplate(new MyOwnEntity(customer.getFirstname(), customer.getEmail(), activationURL, activationURLToPerson),
				"templates/simple.html");
		String formatToPerson = TemplateLoader.loadFilledTemplate(new MyOwnEntity(customer.getFirstname(), customer.getEmail(), activationURL, activationURLToPerson),
				"templates/simple.html");

		boolean multipart = true;
		MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

		if(personFromDb.isEmpty()){
			message.setContent(format, "text/html");
		} else {
			message.setContent(formatToPerson, "text/html");
		}

		helper.setTo(customer.getEmail());
		helper.setSubject("Test message to User App");
		this.emailSender.send(message);

		return _customer;

	}


	@DeleteMapping("/customers/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		System.out.println("Delete Customer with ID = " + id + "...");

		repository.deleteById(id);

		return new ResponseEntity<>("Customer has been deleted!", HttpStatus.OK);
	}

	@DeleteMapping("/customers/delete")
	public ResponseEntity<String> deleteAllCustomers() {
		System.out.println("Delete All Customers...");

		repository.deleteAll();

		return new ResponseEntity<>("All customers have been deleted!", HttpStatus.OK);
	}

	@GetMapping(value = "customers/age/{age}")
	public List<Customer> findByAge(@PathVariable int age) {

		List<Customer> customers = repository.findByAge(age);
		return customers;
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
		System.out.println("Update Customer with ID = " + id + "...");

		Optional<Customer> customerData = repository.findById(id);

		if (customerData.isPresent()) {
			Customer _customer = customerData.get();
			_customer.setLastname(customer.getLastname());
			_customer.setFirstname(customer.getFirstname());
			_customer.setAge(customer.getAge());
			_customer.setEmail(customer.getEmail());
			_customer.setActive(customer.isActive());
			return new ResponseEntity<>(repository.save(_customer), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping("/activate/{uuid}")
	public Person activate(@PathVariable String uuid) {
		Customer customer = repository.findByActivationCode(uuid);
		customer.setActive(true);
		repository.save(customer);
		Person person = new Person(customer.getLastname(), customer.getFirstname(), customer.getAge(), customer.getEmail(),
				String.valueOf(customer.getEmail().hashCode()));
		personRepository.save(person);
		System.out.println(person);

		return person;
	}


}
