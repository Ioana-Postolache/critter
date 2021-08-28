package com.udacity.jdnd.course3.critter.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee extends User {

    @ElementCollection
    @CollectionTable//(name="EMPLOYEE_SKILLS")
    @MapKeyEnumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills;


    @ElementCollection
    @CollectionTable//(name="DAY_OF_WEEK")
    @MapKeyEnumerated(EnumType.STRING)
    private Set<DayOfWeek> daysAvailable;
}
