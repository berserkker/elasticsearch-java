package com.xgy.elasticsearchjava.demo;



import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * String indexName();//索引库的名称，建议以项目的名称命名
 * String type() default "";//类型，建议以实体的名称命名 
 * short shards() default 5;//默认分区数 
 * short replicas() default 1;//每个分区默认的备份数 
 * String refreshInterval() default "1s";//刷新间隔 String
 * indexStoreType() default "fs";//索引文件存储类型
 * 
 * FieldType type() default FieldType.Auto;#自动检测属性的类型
 * FieldIndex index() default FieldIndex.analyzed;#默认情况下分词
 * DateFormat format() default DateFormat.none;
 * String pattern() default ""; 
 * boolean store() default false;#默认情况下不存储原文 String
 * searchAnalyzer() default "";#指定字段搜索时使用的分词器 
 * String indexAnalyzer() default "";#指定字段建立索引时指定的分词器 
 * String[] ignoreFields() default {};#如果某个字段需要被忽略 
 * boolean includeInParent() default false;
 * 
 * @author gc
 *
 */

@Document(indexName = "pepole", type = "employee")
public class Employee {
	@Id
	private int id;
	
	private String firstName;

	private String lastName;

	private Integer age;

	private String about;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
}
