package com.sparta.team30.user.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRoleEnum {
    USER(Authority.CUSTOMER),
    OWNER(Authority.OWNER),
    MANAGER(Authority.MANAGER),
    MASTER(Authority.MASTER);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static List<UserRoleEnum> getHierarchy() {
        return Arrays.asList(MASTER, MANAGER, OWNER, USER);
    }

    public static List<String> getAllAuthorities(UserRoleEnum role) {
        List<UserRoleEnum> hierarchy = getHierarchy();
        int index = hierarchy.indexOf(role);
        return hierarchy.subList(index, hierarchy.size()).stream()
                .map(UserRoleEnum::getAuthority)
                .collect(Collectors.toList());
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String CUSTOMER = ROLE_PREFIX + "CUSTOMER";
        public static final String OWNER = ROLE_PREFIX + "OWNER";
        public static final String MANAGER = ROLE_PREFIX + "MANAGER";
        public static final String MASTER = ROLE_PREFIX + "MASTER";
    }
}