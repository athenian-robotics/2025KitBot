// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Filesystem;

import java.io.File;

import edu.wpi.first.units.*;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    public static final File SWERVE_CONFIG_DIR = new File(Filesystem.getDeployDirectory(), "swerve");

    public static final int LEFT_LEADER_ID = 1;
    public static final int LEFT_FOLLOWER_ID = 2;
    public static final int RIGHT_LEADER_ID = 3;
    public static final int RIGHT_FOLLOWER_ID = 4;

    public static final double MAX_SPEED = 10;

    public static final Current DRIVE_MOTOR_CURRENT_LIMIT = Units.Amps.of(60);
  }

  public static final class RollerConstants {
    public static final int ROLLER_MOTOR_ID = 5;
    public static final Current ROLLER_MOTOR_CURRENT_LIMIT = Units.Amps.of(60);
    public static final Voltage ROLLER_MOTOR_VOLTAGE_COMP = Units.Volts.of(10);
    public static final double ROLLER_EJECT_VALUE = 0.44;
  }

  public static final class OperatorConstants {
    public static final class DriveJoystick {
      public static final int PORT = 0;
      public static final double X_SENSITIVITY = 1;
      public static final double Y_SENSITIVITY = 1;
    }
    public static final class TurnJoystick {
      public static final int PORT = 1;
      public static final double SENSITIVITY = 1;
    }
    public static final class OperatorJoystick {
      public static final int PORT = 2;
    }
  }

  public enum Button {
    TRIGGER(1),
    BOTTOM(2),
    LEFT(3),
    RIGHT(4),
    LEFT_OUTSIDE_TOP(5),
    LEFT_MIDDLE_TOP(6),
    LEFT_INSIDE_TOP(7),
    LEFT_INSIDE_BOTTOM(8),
    LEFT_MIDDLE_BOTTOM(9),
    LEFT_OUTSIDE_BOTTOM(10),
    RIGHT_OUTSIDE_TOP(11),
    RIGHT_MIDDLE_TOP(12),
    RIGHT_INSIDE_TOP(13),
    RIGHT_INSIDE_BOTTOM(14),
    RIGHT_MIDDLE_BOTTOM(15),
    RIGHT_OUTSIDE_BOTTOM(16);

    public final int val;

    Button(int i) {
      val = i;
    }
  }
}
