/* 
 * Copyright (C) 2008 Ma JianLe
 * majianle@gmail.com
 * http://www.tearcry.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */
package cn.tearcry.api.weather;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author rajab
 * 
 */
public class SummaryParser implements Parser {

	private InputSource is;

	private WeatherData wData;

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.tearcry.api.weather.Parser#parse()
	 */
	public void parse() throws WeatherException {
		SAXParserFactory spf = null;
		SAXParser sp = null;
		spf = SAXParserFactory.newInstance();
		DefaultHandler hanlder = new SummaryHanlder(wData);
		try {
			sp = spf.newSAXParser();
			sp.parse(is, hanlder);
			sp = null;
			spf = null;
			hanlder = null;
		} catch (Exception ex) {
			throw new WeatherException(
					"Today and future's weather parse failed.Cause:SAXException");
		}
	}

	public SummaryParser(InputSource is, WeatherData wData)
			throws WeatherException {
		if (is != null)
			this.is = is;
		else
			throw new WeatherException(
					"Today and future's weather parse failed.Cause:Inputsource should not be null!");
		if (wData != null)
			this.wData = wData;
		else
			throw new WeatherException(
					"Today and future's weather parse failed.Cause:Weatherdata should not be null!");
	}

}
