package com.chicu.cakeshop.service;

import com.chicu.cakeshop.model.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getAllSubCategories();
    SubCategory getSubCategoryById(Long id);
    SubCategory saveSubCategory(SubCategory subCategory);
    void deleteSubCategory(Long id);
}

