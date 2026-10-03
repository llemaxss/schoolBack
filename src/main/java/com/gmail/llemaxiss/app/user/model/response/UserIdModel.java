package com.gmail.llemaxiss.app.user.model.response;

import com.gmail.llemaxiss.app._common.model.response.CommonEntityIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Schema(description = "Response model containing only user profile id")
public class UserIdModel extends CommonEntityIdModel {
}
