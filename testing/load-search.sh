docker compose build  --no-cache  && docker compose up --build -d

echo "--------------------------------------------------------------------------------------"
echo "Load testing with Grafana dashboard http://localhost:3000/d/k6/k6-load-testing-results"
echo "--------------------------------------------------------------------------------------"

# check if docker container k6-container is running
while docker ps | grep k6-container > /dev/null; do
  sleep 10
  docker logs k6-container
done

#docker compose down --volumes --remove-orphans postgres price-api