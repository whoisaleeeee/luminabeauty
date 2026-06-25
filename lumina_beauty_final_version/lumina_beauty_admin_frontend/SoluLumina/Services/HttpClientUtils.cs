using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System.Net;
using System.Text.Json;

namespace SoluLumina.Services
{
    public class HttpClientUtils<T>
    {
        public async Task<T> get(string url)
        {
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{url}");
            req.Method = "GET";
            req.Accept = "application/json";
            req.Timeout = 30000;

            using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
            using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
            {
                string json = sr.ReadToEnd();
                T result = JsonConvert.DeserializeObject<T>(json);

                return result;
            }
        }

        public async Task<T> post(string url, Object data)
        {
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{url}");
            req.Method = "POST";
            req.Accept = "application/json";
            req.ContentType = "application/json";
            req.Timeout = 30000;

            string jsonBody = JsonConvert.SerializeObject(data);

            using (StreamWriter sw = new StreamWriter(req.GetRequestStream()))
            {
                sw.Write(jsonBody);
                sw.Flush();
            }

            using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
            using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
            {
                string json = sr.ReadToEnd();
                T result = JsonConvert.DeserializeObject<T>(json);

                return result;
            }
        }

        public async Task<T> delete(string url)
        {
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{url}");
            req.Method = "DELETE";
            req.Accept = "application/json";
            req.Timeout = 30000;

            using (WebResponse res = await req.GetResponseAsync())
            {
                using (Stream stream = res.GetResponseStream())
                {
                    using (StreamReader reader = new StreamReader(stream))
                    {
                        string jsonResponse = await reader.ReadToEndAsync();

                        T resultado = JsonConvert.DeserializeObject<T>(jsonResponse, new JsonSerializerSettings
                        {
                            ContractResolver = new CamelCasePropertyNamesContractResolver()
                        });

                        return resultado;
                    }
                }
            }
        }
    }
}
