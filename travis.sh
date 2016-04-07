git clone https://github.com/non/kind-projector.git &&
git clone https://github.com/xuwei-k/scalacheck.git &&
git clone https://github.com/scalaz/scalaz.git &&
cd kind-projector &&
git checkout v0.7.1 &&
sbt '++ 2.12.0-M4' publishLocal &&
cd ../scalacheck &&
git checkout 1.12.5-update-scalajs &&
sbt '++ 2.12.0-M4' js/publishLocal jvm/publishLocal &&
cd ../scalaz &&
git checkout v7.2.2 &&
sbt '++ 2.12.0-M4' publishLocal &&
cd .. &&
sbt '++ 2.12.0-M4' compile test:compile scalapropsTestNames scalapropsJS/test scalapropsJVM/test "project /" publishLocal
