/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.util.*;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.*;
import com.ctre.phoenix.sensors.CANCoder;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command autonomousCommand;
  private RobotContainer robotContainer;

  //
    double stickY;
    double stickX;

    double stickSense;

    Joystick leftStick;
    Joystick rightStick;

    JoystickButton conveyorButtonTop;
    JoystickButton conveyorButtonBottom;

    JoystickButton conveyorButtonTopDown;
    JoystickButton conveyorButtonBottomDown;

    JoystickButton vannaWhite;

    JoystickButton shooteyBoy;
    
    JoystickButton lifterup;
    JoystickButton lifterdown;
    //Drive motors
    Spark one;
    Spark two;
    Spark three;
    Spark four;

    //Conveyor motors
    Talon five;
    Talon six;

    //Vanna white motor
    Talon seven;

    //Shooter motor
    Talon eight;

    //lifter boy
    Talon nine;

    //lifter encoder
    Encoder lifty;

    //USB Camera
    CameraServer cam;

    //Change the I2C port below to match the connection of your color sensor
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    
    // A Rev Color Sensor V3 object is constructed with an I2C port as a parameter. 
    // The device will be automatically initialized with default parameters.
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

    //A Rev Color Match object is used to register and detect known colors.
    //This object uses a simple euclidian distance to estimate the closest match with given confidence range.
    private final ColorMatch colorMatcher = new ColorMatch();
    
    //Wheel colors
    private final Color wheelBlue = ColorMatch.makeColor(0, 255, 255);
    private final Color wheelGreen = ColorMatch.makeColor(0, 255, 0);
    private final Color wheelRed = ColorMatch.makeColor(255, 0, 0);
    private final Color wheelYellow = ColorMatch.makeColor(255, 255, 0);


    //Initializes encoder
    CANCoder encocoder;
    int countLoop;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    robotContainer = new RobotContainer();
    
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    
    conveyorButtonTop = new JoystickButton(rightStick, 5);
    conveyorButtonBottom = new JoystickButton(rightStick, 3);

    conveyorButtonTopDown = new JoystickButton(rightStick, 6);
    conveyorButtonBottomDown = new JoystickButton(rightStick, 4);


    vannaWhite = new JoystickButton(rightStick, 2);

    shooteyBoy = new JoystickButton(rightStick, 1);

    lifterup = new JoystickButton(rightStick, 11);
    lifterdown = new JoystickButton(rightStick, 12);


    //drive motors
    one = new Spark(0);
    two = new Spark(1);
    three = new Spark(2);
    four = new Spark(3);
    
    //conveyor motors
    five = new Talon(4);
    six = new Talon(5);

    //vana white motor
    seven = new Talon(6);

    //shooty motor
    eight = new Talon(7);

    //lift motor
    nine = new Talon(8);


    //cammera hacccker
    cam = CameraServer.getInstance();
    cam.startAutomaticCapture();

    //encoderr hack
    encocoder = new CANCoder(1); 
    countLoop = 0;

    //wheels color 
    colorMatcher.addColorMatch(wheelBlue);
    colorMatcher.addColorMatch(wheelGreen);
    colorMatcher.addColorMatch(wheelRed);
    colorMatcher.addColorMatch(wheelYellow);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    Color detectedColor = colorSensor.getColor();

    String colorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

    if (match.color == wheelBlue) {
      colorString = "Blue";
    } else if (match.color == wheelGreen) {
      colorString = "Red";
    } else if (match.color == wheelRed) {
      colorString = "Green";
    } else if (match.color == wheelYellow) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    autonomousCommand = robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {



     //Encoder output
     if(countLoop++ > 10)
     {
         countLoop = 0;
         double degrees =  encocoder.getPosition();
         System.out.println("CANCoder position is: " + degrees);
     }


    //Setting variable equal to the positions in left stick
    //used so we can drive with one stick
    stickY = leftStick.getY();
    stickX = leftStick.getX();
    stickSense = leftStick.getRawAxis(3) + 2;


    //Drive code
    //Optimize it for command based code when you have time

    //Right Side
    one.set((stickY + stickX) / stickSense);
    two.set((stickY + stickX) / stickSense);

    //Left side
    three.set((-stickY + stickX) / stickSense);
    four.set((-stickY + stickX) / stickSense);

    //Sucky buttons
    if(conveyorButtonBottom.get())
    {
      five.set(-0.8);
    }
    else
    {
      five.set(0.0);
    }

    
    if(conveyorButtonTop.get())
    {
      six.set(-0.8);
    }
    else
    {
      six.set(0.0);
    }

    //Sucky Buttons ejecting
    if(conveyorButtonBottomDown.get())
    {
      five.set(0.8);
    }
    else
    {
      five.set(0.0);
    }

    if(conveyorButtonTopDown.get())
    {
      six.set(0.8);
    }
    else
    {
      six.set(0.0);
    }

    //vannaWhite Buttons
    if(vannaWhite.get())
    {
      seven.set(-0.5);
    }
    else
    {
      seven.set(0.0);
    }

    //Shooty Buttons
    if(shooteyBoy.get())
    {
      eight.set(-1.0);
    }
    else
    {
      eight.set(0.0);
    }
    

    if(lifterup.get())
    {
      nine.set(-0.5);
    }
    else
    {
      nine.set(0.0);
    }

    if(lifterdown.get())
    {
      nine.set(0.5);
    }
    else
    {
      nine.set(0.0);
    }


  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
