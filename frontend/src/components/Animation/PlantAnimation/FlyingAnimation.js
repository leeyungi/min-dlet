import React, { useState, Suspense, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import DandelionUp from '../DandelionUp';
import DandelionPetalUp from '../DandelionPetalUp';
import DandelionSeedDown from '../DandelionSeedDown';
import LoadingImg from 'assets/images/mindletGif.gif';
import Grass from '../Grass';

function FallbackTitle() {
  return (
    <Html center>
      <h1 style={{ color: 'white' }}>
        <br />
        <img src={LoadingImg} alt="loading" />
        <br />
      </h1>
    </Html>
  );
}
export default function FlyingAnimation({ endstate, msgCheck, isPossible }) {
  const [nextstate, SetNext] = useState(false);
  const [grassState, SetGrass] = useState(false);
  const [dandelstate, SetDandel] = useState(false);
  const [pagestate, SetPage] = useState(false);

  const touch = () => {
    msgCheck(1);
    //  console.log(isPossible);
  };

  const handleNext = (next) => {
    SetNext(next);
  };

  const down = (next) => {
    SetGrass(next);
  };

  const petalUp = (petals) => {
    SetDandel(petals);
  };

  const petalAppear = (petal) => {
    SetPage(petal);
  };

  useEffect(() => {
    //  console.log(pagestate);
    if (pagestate === 1) endstate(true);
  }, [pagestate]);

  useEffect(() => {
    //   console.log(nextstate);
    if (nextstate === true) down(true);
  }, [nextstate]);

  useEffect(() => {
    //   console.log(dandelstate);
    if (dandelstate === true) {
      //
      petalUp(true);
    }
  }, [dandelstate]);

  return (
    <Canvas
      frameloop="demand"
      camera={{
        position: [60, -50, 110],
        // <Canvas frameloop="demand" style={{pointerEvents: 'auto', cursor:'pointer'}} pixelRatio={[1, 1]} camera={{ position: [-15, 27, 150],
        fov: 90,
        far: 500,
        near: 10,
      }}
      onClick={touch}
    >
      <ambientLight intensity={0.3} />
      <Suspense fallback={<FallbackTitle />}>
        <Grass downgrass={grassState} />
        <DandelionSeedDown flag={handleNext} />
        <directionalLight position={[0.5, 1, 0.866]} intensity={1.7} />
        <DandelionUp dandelUp={grassState} petal={petalUp} />
        {dandelstate && <DandelionPetalUp flag={petalAppear} />}
        <directionalLight position={[0.5, 10, 0.866]} intensity={1.7} />
        <directionalLight position={[-1, -0.3, -0.866]} intensity={1} />
      </Suspense>
      <OrbitControls
        enableRotate={false}
        enablePan={false}
        autoRotate={false}
        minDistance={120}
        maxDistance={150}
      />
    </Canvas>
  );
}
