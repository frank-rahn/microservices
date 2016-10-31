package de.rahn.finances.server.web.ui;

import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Workaround für <a href="https://github.com/spring-projects/spring-boot/issues/5638">Bug 5638</a>. Durch diesen Bug wird auf
 * der Error-Seite kein
 * Security Context zu Verfügung gestellt. Angeblich nur wenn spring-boot-actuator im Klassenpfad ist ...
 *
 * @author hv11h36
 */
@Controller
public class WorkaroundErrorController extends BasicErrorController {

	/**
	 * {@inheritDoc}
	 */
	@Autowired
	public WorkaroundErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes, new ErrorProperties() {
			{
				setIncludeStacktrace(ALWAYS);
			}
		});
	}

	@RequestMapping(value = "/error")
	public String error() {
		return "error";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorPath() {
		return "/__dummyErrorPath";
	}

}