package bts.delation.model.dto;

public record ProfileUserDto(
            String id,
            String email,
            String role,
            boolean synced,
            String discordId,
            String discordUsername,
            String mineUsername,
            boolean mineSynced
    ) {
    }