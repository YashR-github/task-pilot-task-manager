package org.example.taskpilot_taskmanager.task.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.taskpilot_taskmanager.task.dtos.TaskFilterRequestDTO;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;



/**
 * Builds filtering predicates for Task queries based on TaskFilterRequestDTO.
 * Sorting is handled separately by SortPageableUtil.
 */
public class TaskSpecification implements Specification<Task> {

    private final TaskFilterRequestDTO filter;

    public TaskSpecification(TaskFilterRequestDTO filter) {
        this.filter = filter;
    }

    /**
     * Builds a specification to restrict tasks to the given user.
     */
    public static Specification<Task> belongsToUser(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    @Override
    public Predicate toPredicate(Root<Task> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filter by keyword in name or description (covers name searches)
        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            String pattern = "%" + filter.getKeyword().toLowerCase() + "%";
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(root.get("name")), pattern),
                            cb.like(cb.lower(root.get("description")), pattern)
                    )
            );
        }

        // Filter by status
        if (filter.getTaskStatus() != null) {
            predicates.add(cb.equal(root.get("taskStatus"), filter.getTaskStatus()));
        }

        // Filter by priority
        if (filter.getTaskPriority() != null) {
            predicates.add(cb.equal(root.get("taskPriority"), filter.getTaskPriority()));
        }

        // Filter by category
        if (filter.getCategoryName() != null) {
            predicates.add(cb.equal(
                    cb.lower(root.get("category").get("name")),
                    filter.getCategoryName().toLowerCase()
            ));
        }
        if (filter.getCategoryType() != null) {
            predicates.add(cb.equal(root.get("category").get("categoryType"), filter.getCategoryType()));
        }

        // Filter by creation date range
        if (filter.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter()));
        }
        if (filter.getCreatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedBefore()));
        }

        // Combine all predicates
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}




// Old version
//public class TaskSpecification implements Specification<Task>{
//
//    private final TaskFilterRequestDTO filter;
//
//    public TaskSpecification(TaskFilterRequestDTO filter){
//        this.filter = filter;
//    }
//
//
//
//    public static Specification<Task> belongsToUser(User user){
//        return (root, query, cb) -> cb.equal(root.get("user"),user);
//
//    }
//
//
//
//    @Override
//    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb){
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Filter by task name (partial match, case-insensitive)
//        if (filter.getName() != null) {
//            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
//        }
//
//        // Filter by keyword in name or description
//        if (filter.getKeyword() != null) {
//            Predicate nameLike = cb.like(cb.lower(root.get("name")), "%" + filter.getKeyword().toLowerCase() + "%");
//            Predicate descLike = cb.like(cb.lower(root.get("description")), "%" + filter.getKeyword().toLowerCase() + "%");
//            predicates.add(cb.or(nameLike, descLike));
//        }
//
//        // Filter by task status
//        if (filter.getTaskStatus() != null) {
//            predicates.add(cb.equal(root.get("taskStatus"), filter.getTaskStatus()));
//        }
//
//        // Filter by task priority
//        if (filter.getTaskPriority() != null) {
//            predicates.add(cb.equal(root.get("taskPriority"), filter.getTaskPriority()));
//        }
//
//        // Filter by category name
//        if (filter.getCategoryName() != null) {
//            predicates.add(cb.equal(root.get("category").get("name"), filter.getCategoryName()));
//        }
//
//        // Filter by category type (enum)
//        if (filter.getCategoryType() != null) {
//            predicates.add(cb.equal(root.get("category").get("categoryType"), filter.getCategoryType()));
//        }
//
//        // Filter by creation date range
//        if (filter.getCreatedAfter() != null) {
//            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter()));
//        }
//
//        if (filter.getCreatedBefore() != null) {
//            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedBefore()));
//        }
//
//        // Sort logic
//        String sortBy = (filter.getSortBy() != null) ? filter.getSortBy() : "createdAt";
//        if ("desc".equalsIgnoreCase(filter.getSortDirection())) {
//            query.orderBy(cb.desc(root.get(sortBy)));
//        } else {
//            query.orderBy(cb.asc(root.get(sortBy)));
//        }
//
//        return cb.and(predicates.toArray(new Predicate[0]));
//    }
//}

//  alternate approach if not overriding toPredicate and not extending Specification<Task>
//    public  static Specification<Task> getFilteredTasks(TaskFilterRequestDTO filter){
//        return (root , query, cb)->{
//            List<Predicate> predicates= new ArrayList<>();
//            if(filter.getName() != null){
//                predicates.add(cb.like(cb.lower(root.get("name")), "%"  + filter.getName().toLowerCase() + "%" )) ;
//            }
//            if(filter.getKeyword()!=null)
//}