package pn.nutrimeter.data.models.specifications.builder;

import org.springframework.data.jpa.domain.Specification;
import pn.nutrimeter.data.models.specifications.JoinCriteria;
import pn.nutrimeter.data.models.specifications.SearchCriteria;

import java.util.function.Function;

public interface SpecificationBuilder<T> {
    SpecificationBuilder<T> with(SearchCriteria searchCriteria);

    <E> SpecificationBuilder<T> join(JoinCriteria joinCriteria);

    Specification<T> build(Function<SearchCriteria, Specification<T>> mapToSpecification);
}
