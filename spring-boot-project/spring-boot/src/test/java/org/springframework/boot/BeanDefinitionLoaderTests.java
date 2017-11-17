/*
 * Copyright 2012-2017 the original author or authors.
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

package org.springframework.boot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sampleconfig.MyComponentInPackageWithoutDot;

import org.springframework.boot.sampleconfig.MyComponent;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BeanDefinitionLoader}.
 *
 * @author Phillip Webb
 */
public class BeanDefinitionLoaderTests {

	private StaticApplicationContext registry;

	@Before
	public void setup() {
		this.registry = new StaticApplicationContext();
	}

	@After
	public void cleanUp() {
		this.registry.close();
	}

	@Test
	public void loadClass() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadXmlResource() throws Exception {
		ClassPathResource resource = new ClassPathResource("sample-beans.xml",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myXmlComponent")).isTrue();

	}

	@Test
	public void loadGroovyResource() throws Exception {
		ClassPathResource resource = new ClassPathResource("sample-beans.groovy",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();

	}

	@Test
	public void loadGroovyResourceWithNamespace() throws Exception {
		ClassPathResource resource = new ClassPathResource("sample-namespace.groovy",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();

	}

	@Test
	public void loadPackage() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadClassName() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getName());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadResourceName() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				"classpath:org/springframework/boot/sample-beans.xml");
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myXmlComponent")).isTrue();
	}

	@Test
	public void loadGroovyName() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				"classpath:org/springframework/boot/sample-beans.groovy");
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();
	}

	@Test
	public void loadPackageName() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage().getName());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadPackageNameWithoutDot() throws Exception {
		// See gh-6126
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponentInPackageWithoutDot.class.getPackage().getName());
		int loaded = loader.load();
		assertThat(loaded).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponentInPackageWithoutDot")).isTrue();
	}

	@Test
	public void loadPackageAndClassDoesNotDoubleAdd() throws Exception {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage(), MyComponent.class);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

}
