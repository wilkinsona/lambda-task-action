package com.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

public class ExamplePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getTasks().withType(JavaCompile.class, (compile) -> {
			compile.doFirst((task) -> {});
		});
	}

}
