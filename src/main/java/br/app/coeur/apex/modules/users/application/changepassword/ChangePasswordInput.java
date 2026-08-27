package br.app.coeur.apex.modules.users.application.changepassword;

import java.util.UUID;

public record ChangePasswordInput(UUID id, String currentPassword, String newPassword) {
}
