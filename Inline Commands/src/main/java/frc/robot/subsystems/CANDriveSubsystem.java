// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.motors.TalonFXSwerve;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;

public class CANDriveSubsystem extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  public CANDriveSubsystem() {
    try {
      swerveDrive = new SwerveParser(Constants.DriveConstants.SWERVE_CONFIG_DIR).createSwerveDrive(Constants.DriveConstants.MAX_SPEED);
    } catch (Exception e) 
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void periodic() {
  }

  // Command to drive the robot with transformations
  public Command driveCommand(CANDriveSubsystem driveSubsystem, DoubleSupplier xTranslation, DoubleSupplier yTranslation, DoubleSupplier zRotation) {
    return Commands.run(
        () -> swerveDrive.drive (new Translation2d(xTranslation.getAsDouble(), yTranslation.getAsDouble()), zRotation.getAsDouble(), true, false), driveSubsystem);
  }
}
