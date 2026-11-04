package com.gmail.llemaxiss.app.user.util;

import java.util.Set;
import java.util.UUID;

public interface UserConstants {

  UUID ADMIN_USER_ID = UUID.fromString("00000000-0000-4000-a000-000000000001");
  UUID SYSTEM_USER_ID = UUID.fromString("00000000-0000-4000-a000-000000000002");

  Set<UUID> PROTECTED_USER_IDS = Set.of(ADMIN_USER_ID, SYSTEM_USER_ID);
}
