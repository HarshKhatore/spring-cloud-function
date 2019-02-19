/*
 * Copyright 2012-2019 the original author or authors.
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
 */

package org.springframework.cloud.function.web.source;

import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.function.web.RestApplication;
import org.springframework.cloud.function.web.source.SourceAutoConfigurationIntegrationTests.ApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, //
		properties = "spring.cloud.function.web.supplier.templateUrl=http://localhost:9999/notthere")
@ContextConfiguration(classes = { RestApplication.class, ApplicationConfiguration.class })
public class SourceAutoConfigurationIntegrationTests {

	@Autowired
	private SupplierExporter forwarder;

	@Test
	public void fails() throws Exception {
		int count = 0;
		while (this.forwarder.isRunning() && count++ < 1000) {
			Thread.sleep(50);
		}
		// It completed
		assertThat(this.forwarder.isRunning()).isFalse();
		// But failed
		assertThat(this.forwarder.isOk()).isFalse();
	}

	@EnableAutoConfiguration
	@TestConfiguration
	public static class ApplicationConfiguration {

		@Bean
		public Supplier<String> word() {
			return () -> "foo";
		}

	}

}