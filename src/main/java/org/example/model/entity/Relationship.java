package org.example.model.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Relationship {
	private final List<ReferencingField> fields;
}