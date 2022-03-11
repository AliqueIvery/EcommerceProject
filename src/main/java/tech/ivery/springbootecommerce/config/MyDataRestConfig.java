package tech.ivery.springbootecommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import tech.ivery.springbootecommerce.models.Product;
import tech.ivery.springbootecommerce.models.ProductCategory;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{
	
	@Autowired
	private EntityManager entityManager;
	
	public MyDataRestConfig(EntityManager e) {
		this.entityManager = e;
	}
	//Makes code read only
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		// TODO Auto-generated method stub
		HttpMethod[] theUnsupportedActions = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT};
		
		//disable http methods for Product Repository
		config.getExposureConfiguration().forDomainType(Product.class)
		.withItemExposure((metdata , httpMethods) -> httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata , httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		//disable http methods for ProductCategory Repository
		config.getExposureConfiguration().forDomainType(ProductCategory.class)
		.withItemExposure((metdata , httpMethods) -> httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata , httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		//call internal helper to expose ids
		exposeIds(config);
	}

	private void exposeIds(RepositoryRestConfiguration config) {
		//get list of all entity classes from entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		//create arraylist of entity types
		List<Class> entityClasses = new ArrayList<>();
		
		for(EntityType tempClass: entities ) {
			entityClasses.add(tempClass.getJavaType());
		}
		
		//expose entity ids
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
	}
	

}
