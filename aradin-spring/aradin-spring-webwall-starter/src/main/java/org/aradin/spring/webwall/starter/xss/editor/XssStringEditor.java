package org.aradin.spring.webwall.starter.xss.editor;

import java.beans.PropertyEditorSupport;

import org.aradin.spring.webwall.starter.xss.XssConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

public class XssStringEditor extends PropertyEditorSupport implements WebBindingInitializer {

	/**
	 * Gets the property value as a string suitable for presentation to a human to
	 * edit.
	 *
	 * @return The property value as a string suitable for presentation to a human
	 *         to edit.
	 *         <p>
	 *         Returns null if the value can't be expressed as a string.
	 *         <p>
	 *         If a non-null value is returned, then the PropertyEditor should be
	 *         prepared to parse that string back in setAsText().
	 */
	@Override
	public String getAsText() {
		return (getValue() != null) ? XssConverter.xssEncode(getValue().toString()) : null;
	}

	/**
	 * Sets the property value by parsing a given String. May raise
	 * java.lang.IllegalArgumentException if either the String is badly formatted or
	 * if this kind of property can't be expressed as text.
	 *
	 * @param text The string to be parsed.
	 */
	@Override
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
		setValue(XssConverter.xssEncode(text));
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		// TODO Auto-generated method stub
		binder.registerCustomEditor(String.class, this);
	}
}
