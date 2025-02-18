package com.sparta.team30.category.domain;

import com.sparta.team30.category.dto.CategoryRequestDto;
import com.sparta.team30.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_category")
public class Category extends BaseEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR) //H2?
    @Column(name = "category_id", updatable = false, nullable = false)
    private UUID categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

    public Category(CategoryRequestDto requestDto) {
        this.categoryName = requestDto.getCategoryName();
    }

    public void update(CategoryRequestDto requestDto) {
        this.categoryName = requestDto.getCategoryName();
    }
}
