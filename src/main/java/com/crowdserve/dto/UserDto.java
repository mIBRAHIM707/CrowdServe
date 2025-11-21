package com.crowdserve.dto;

public record UserDto(
    Long id,
    String username,
    String fullName,
    String email,
    String bio,
    Double averageRating
) {
}
