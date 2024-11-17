package org.example.service;

import java.io.File;
import java.util.List;
import org.example.model.entity.Entity;

public interface TemplateService {
	void process(List<Entity>entities, File outputDirectory);
}
