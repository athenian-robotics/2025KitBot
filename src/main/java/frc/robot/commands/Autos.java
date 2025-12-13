// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CANDriveSubsystem;

public final class Autos {
  static Timer timer = new Timer();
  static double lessAcceleration = 10.0;

  // Example autonomous command which drives forward for 1 second.
  public static final Command exampleAuto(CANDriveSubsystem driveSubsystem) {
    return driveSubsystem.driveArcade(driveSubsystem, () -> 0.5, () -> 0.0).withTimeout(1.0);
  }

  public static final Command circleAuto(CANDriveSubsystem driveSubsystem) {
    return new InstantCommand(() -> {
      timer.start();
    }).andThen(driveSubsystem.driveArcade(driveSubsystem, () -> timer.get() / lessAcceleration, () -> 0.5).withTimeout(10.0));
  }
}
