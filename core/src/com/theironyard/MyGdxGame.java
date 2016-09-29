package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion stand, jump;
	boolean canJump = true;
	boolean faceRight = true;
	Animation walk;
	float x, y, xv, yv, totalTime;

	static final float MAX_VELOCITY = 500;
	static final float FRICTION = 0.9f;
	static final int WIDTH = 18;
	static final int HEIGHT = 26;
	static final int DRAW_WIDTH = WIDTH *3;
	static final int DRAW_HEIGHT = HEIGHT *3;
	static final int GRAVITY = -100;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("koalio.png");
		TextureRegion[][] tiles = TextureRegion.split(img, WIDTH, HEIGHT);
		stand = tiles[0][0];
		jump = tiles[0][1];
		walk = new Animation(0.08f, tiles[0][2],tiles[0][3],tiles[0][4]);
	}

	@Override
	public void render () {

		// keep track of total time
		// totalTime = totalTime +Gdx.graphics.getDeltaTime();  OR
		totalTime += Gdx.graphics.getDeltaTime();

		move();

		TextureRegion koalio;
		if (y > 0) {
			koalio = jump;
		}
		else if (xv != 0) {
			koalio = walk.getKeyFrame(totalTime,true);
		}
		else {
			koalio = stand;
		}
		Gdx.gl.glClearColor(0.5f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (faceRight) {
			batch.draw(koalio, x, y, DRAW_WIDTH, DRAW_HEIGHT);
		}
		else {
			batch.draw(koalio, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void move () {
		if ((Gdx.input.isKeyPressed(Input.Keys.W) && canJump) || (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump)) {
			yv = MAX_VELOCITY * 4;
			canJump = false;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			yv = MAX_VELOCITY * -1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = MAX_VELOCITY;
			faceRight = true;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			xv = MAX_VELOCITY * -1;
			faceRight = false;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q) && canJump) {
			xv = MAX_VELOCITY * -1;
			yv = MAX_VELOCITY * 4;
			canJump = false;
			faceRight = false;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.E) && canJump) {
			xv = MAX_VELOCITY;
			yv = MAX_VELOCITY * 4;
			canJump = false;
			faceRight = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)&& canJump) {
			yv = MAX_VELOCITY * 7;
			canJump = false;
		}

		yv += GRAVITY;

		x += xv * Gdx.graphics.getDeltaTime();
		y += yv * Gdx.graphics.getDeltaTime();

		xv = decelerate(xv);
		yv = decelerate(yv);

		if (y < 0) {
			y = 0;
			canJump = true;
		}

		System.out.println(xv + " " + yv);
	}

	public float decelerate(float velocity) {
		velocity *= FRICTION;
		if (Math.abs(velocity) < 40) {
			velocity = 0;
		}
		return velocity;
	}
}
