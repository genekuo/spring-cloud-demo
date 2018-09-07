package demo.spring.search.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.search.component.SearchComponent;
import demo.spring.search.entity.Flight;

@CrossOrigin
@RestController
@RefreshScope
@RequestMapping("/search")
class SearchRestController {

	private static final Logger logger = LoggerFactory.getLogger(SearchRestController.class);

	private SearchComponent searchComponent;

	@Value("${originairports.shutdown}")
	private String originAirportsShutdownList;

	@Autowired
	public SearchRestController(SearchComponent searchComponent) {
		this.searchComponent = searchComponent;
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	List<Flight> search(@RequestBody SearchQuery query) {
		logger.info("Input : " + query);
		logger.info("originAirportsShutdownList : " + originAirportsShutdownList);
		if (Arrays.asList(originAirportsShutdownList.split(",")).contains(query.getOrigin())) {
			logger.info("The origin airport is in shutdown state");
			return new ArrayList<Flight>();
		}
		return searchComponent.search(query);
	}

	@RequestMapping("/hub")
	String getHub() {
		logger.info("Searching for Hub, received from search-apigateway ");
		return "SFO";
	}

}
